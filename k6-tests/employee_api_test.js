import http from 'k6/http';
import { check, sleep, fail } from 'k6';
import { htmlReport } from 'https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.0.1/index.js';
export const options = {
    stages: [
        { duration: '10s', target: 10 }, // ramp up to 20 users
        { duration: '1m', target: 50 },  // stay at 20 users
        { duration: '10s', target: 0 },  // scale down
    ],
    thresholds: {
        'http_req_duration{name:Login}': ['p(95)<1000'],          // Login threshold (1s)
        'http_req_duration{name:FetchEmployees}': ['p(95)<500'], // Fetch threshold (500ms)
        'http_req_duration{name:AddEmployee}': ['p(95)<1500'],   // Add threshold (1.5s)
        'iteration_duration': ['p(95)<5000'],                    // Iteration threshold (5s - including sleep)
    },
};

const BASE_URL = 'http://localhost:8080';

export default function () {
    // 1. Login to obtain the JWT token
    // Disable redirects to capture the Set-Cookie header from the 302 Redirect
    console.log("testing login");
    let loginRes = http.post(`${BASE_URL}/auth/login`, {
        username: 'crestasom',
        password: 'test',
    }, {
        redirects: 0,
        tags: { name: 'Login' }
    });

    const loginSuccess = check(loginRes, {
        'login redirected (success)': (r) => r.status === 302,
    });

    if (!loginSuccess) {
        console.error(`Login failed for VU ${__VU}, skipping iteration.`);
        return;
    }
    console.log("testing login success");
    // Extract JWT from cookie
    const jwt = loginRes.cookies['jwt'] ? loginRes.cookies['jwt'][0].value : null;

    const jwtOk = check(jwt, {
        'jwt token is present in cookie': (t) => t !== null && t !== undefined,
    });

    if (!jwtOk) {
        console.error(`JWT not found in cookie for VU ${__VU}, skipping iteration.`);
        return;
    }

    const authParams = {
        headers: {
            'Authorization': `Bearer ${jwt}`,
            'Content-Type': 'application/json',
        },
    };
    console.log("testing get all employee");
    // 2. Test GET all employees
    let res = http.get(`${BASE_URL}/api/employees`, {
        ...authParams,
        tags: { name: 'FetchEmployees' }
    });
    check(res, {
        'get employees status is 200': (r) => r.status === 200,
    });
    console.log("testing post employee");
    // 3. Test POST a new employee
    const payload = JSON.stringify({
        name: `K6 User ${__VU}-${__ITER}`,
        email: `k6user_${__VU}_${__ITER}_${Date.now()}@example.com`,
        contact: '9800000000',
        position: 'Tester',
    });

    res = http.post(`${BASE_URL}/api/employees`, payload, {
        ...authParams,
        tags: { name: 'AddEmployee' }
    });
    check(res, {
        'post employee status is 200': (r) => r.status === 200,
    });

    sleep(1);
    console.log("testing one cycle complete");
}

// Automatically generate reports after test
export function handleSummary(data) {
    return {
        'summary.html': htmlReport(data),
        stdout: textSummary(data, { indent: ' ', enableColors: true }),
    };
}
