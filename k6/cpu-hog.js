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
    http.get('http://54.253.135.248cpu-stress?l=1&r=300000000');
}