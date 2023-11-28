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

export const getAllDonVi = () => {
    return axios.get(`${ip3}/api/v1/organizations`)
}

// export const getSentEdocList = (headers: any) => {
//     return axios.get(`${ip3}/api/v1/document/edocs/getSentEdocList`, {
//       headers: headers
//     })
// }

// export const sendEdoc = (headers: any, payload: any) => {
//     return axios.post(`${ip3}/api/v1/document/edocs/new`, payload, {
//       headers: headers
//     })
// }

// export const sendStatusEdoc = (headers: any, payload: any) => {
//     return axios.post(`${ip3}/api/v1/document/status/update`, payload, {
//       headers: headers
//     })
// }