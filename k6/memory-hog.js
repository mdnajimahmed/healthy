import http from 'k6/http';
import {check, sleep} from 'k6';

export const options = {
    stages: [
        {duration: '90s', target: 20},
        {duration: '30s', target: 10},
        {duration: '30s', target: 0},
    ],
};

export default function () {
    const res = http.get('http://54.253.135.248/memory-stress');
    // sleep(1)
    // check(res, {'status was 200': (r) => r.status == 200});
}