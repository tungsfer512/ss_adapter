import { ip3 } from '@/utils/ip';
import axios from 'axios';

export async function getMember() {
  return axios.get(`${ip3}/api/v1/members`);
}

export async function getService(payload: any) {
  return axios.get(`${ip3}/api/v1/services`, {
    headers: {
      'Subsystem-Code': payload['Subsystem-Code'],
      'Content-Type': 'application/json',
      'X-Road-Client': payload['X-Road-Client'],
    }
  });
}
export async function getEndpoint(payload: any) {
  return axios.get(`${ip3}/api/v1/getEndpoints`, {
    headers: {
      'Subsystem-Code': payload['Subsystem-Code'],
      'Service-Code': payload['Service-Code'],
      'Service-Endpoint': payload['Service-Endpoint'],
      'Content-Type': 'application/json',
      'X-Road-Client': payload['X-Road-Client'],
    }
  });
}