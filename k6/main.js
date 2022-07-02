import http from 'k6/http';
import {check, sleep} from 'k6';

export const options = {
    stages: [
        {duration: '300s', target: 20},
        {duration: '30s', target: 10},
        {duration: '30s', target: 0},
    ],
};

export default function () {
    const res = http.get('https://4e5qkvhp74.execute-api.ap-southeast-2.amazonaws.com/Prod/1');
    check(res, {'status was 200': (r) => r.status == 200});
    // const url = 'https://4e5qkvhp74.execute-api.ap-southeast-2.amazonaws.com/Prod';
    //
    // const payload = JSON.stringify({
    //     "id": "1",
    //     "name": "Java"
    // });
    //
    // const params = {
    //     headers: {
    //         'Content-Type': 'application/json',
    //     },
    // };
    //
    // http.post(url, payload, params);
}