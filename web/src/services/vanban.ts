/* eslint-disable no-param-reassign */
/* eslint-disable no-underscore-dangle */
/* eslint-disable @typescrip3t-eslint/naming-convention */
import axios from '@/utils/axios';
import { ip3 } from '@/utils/ip';


export interface IPayload {
  page: number,
  limit: number,
  cond: object,
}

export const getReceivedEdocList = (headers: any) => {
    return axios.get(`${ip3}/api/v1/edocs/getReceivedEdocList`, {
      headers: headers
    })
}

export const getSentEdocList = (headers: any) => {
    return axios.get(`${ip3}/api/v1/edocs/getSentEdocList`, {
      headers: headers
    })
}

export const sendEdoc = (headers: any, payload: any) => {
    return axios.post(`${ip3}/api/v1/edocs/newedoc`, payload, {
      headers: headers
    })
}

export const sendStatusEdoc = (headers: any) => {
    return axios.post(`${ip3}/api/v1/edocs/status/update`, undefined, {
      headers: headers
    })
}