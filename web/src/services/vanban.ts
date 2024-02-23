/* eslint-disable no-param-reassign */
/* eslint-disable no-underscore-dangle */
/* eslint-disable @typescrip4t-eslint/naming-convention */
import axios from '@/utils/axios';
import { ip4 } from '@/utils/ip';


export interface IPayload {
  page: number,
  limit: number,
  cond: object,
}

export const getReceivedEdocList = (headers: any) => {
  return axios.get(`${ip4}/api/v1/document/edocs/getReceivedEdocList`, {
    headers: {
      ...headers,
      organizationId: "CS:GOV:SS1MC:SS1SUB1"
    }
  })
}

export const getSentEdocList = (headers: any) => {
  return axios.get(`${ip4}/api/v1/document/edocs/getSentEdocList`, {
    headers: {
      ...headers,
      organizationId: "CS:GOV:SS1MC:SS1SUB1"
    }
  })
}

export const sendEdoc = (headers: any, payload: any) => {
  return axios.post(`${ip4}/api/v1/document/edocs/new`, payload, {
    headers: {
      ...headers,
      organizationId: "CS:GOV:SS1MC:SS1SUB1"
    }
  })
}

export const sendStatusEdoc = (headers: any, payload: any) => {
  return axios.post(`${ip4}/api/v1/document/status/new`, payload, {
    headers: headers
  })
}