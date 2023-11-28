import { message } from 'antd';
import { useRef, useState } from 'react';
import { getReceivedEdocList, getSentEdocList, sendEdoc, sendStatusEdoc } from '@/services/vanban';
import { IVanBanRecord } from '@/utils/interfaces';

export default () => {
  const [danhSach, setDanhSach] = useState<IVanBanRecord[]>([]);
  const [loading, setLoading] = useState(false);
  const [edit, setEdit] = useState(false);
  const [record, setRecord] = useState<IVanBanRecord | {}>({});
  const [visibleForm, setVisibleForm] = useState(false);
  const [total, setTotal] = useState(0);
  const [page, setPage] = useState(1);
  const [limit, setLimit] = useState(10);

  const get_sent_edoc_list = async (headers: any) => {
    try {
      setLoading(true);
      const res = await getSentEdocList(headers);
      if (res.status === 200) {
        setDanhSach(res.data?.data);
        setTotal(res.data?.data?.length);
      }
      setLoading(false);
    } catch (error: any) {
      console.log(error?.message);
      message.error(error?.message);
    } finally {
      setLoading(false);
    }
  };

  const get_received_edoc_list = async (headers: any) => {
    try {
      setLoading(true);
      const res = await getReceivedEdocList(headers);
      if (res.status === 200) {
        setDanhSach(res.data?.data);
        setTotal(res.data?.data?.length);
      }
      setLoading(false);
    } catch (error: any) {
      console.log(error?.message);
      message.error(error?.message);
    } finally {
      setLoading(false);
    }
  };

  const send_edoc = async (headers: any, payload: any) => {
    try {
      setLoading(true);
      const res = await sendEdoc(headers, payload);
      if (res.status === 200) {
        const res_get = await getReceivedEdocList(headers);
        if (res_get.status === 200) {
          setDanhSach(res_get.data?.data);
          setTotal(res_get.data?.data?.length);
        }
      }
      setLoading(false);
    } catch (error: any) {
      console.log(error?.message);
      message.error(error?.message);
    } finally {
      setLoading(false);
    }
  };

  const send_status_edoc = async (headers: any, payload: any) => {
    try {
      setLoading(true);
      const res = await sendStatusEdoc(headers, payload);
      if (res.status === 200) {
        const res_get = await getReceivedEdocList(headers);
        if (res_get.status === 200) {
          setDanhSach(res_get.data?.data);
          setTotal(res_get.data?.data?.length);
        }
      }
      setLoading(false);
    } catch (error: any) {
      console.log(error?.message);
      message.error(error?.message);
    } finally {
      setLoading(false);
    }
  };

  return {
    danhSach,
    loading,
    edit,
    limit,
    total,
    page,
    record,
    visibleForm,
    setDanhSach,
    setLoading,
    setEdit,
    setLimit,
    setTotal,
    setPage,
    setRecord,
    setVisibleForm,
    get_received_edoc_list,
    get_sent_edoc_list,
    send_edoc,
    send_status_edoc,
  };
};
