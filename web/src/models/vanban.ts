import { message } from 'antd';
import { useRef, useState } from 'react';
import { getReceivedEdocList, getSentEdocList } from '@/services/vanban';

export interface IVanBanRecord {
  id: string;
  senderDocId: string;
  receiverDocId: string;
  pid: string;
  serviceType: string;
  messageType: string;
  created_time: string;
  updated_time: string;
  from: string;
  to: string;
  sendStatus: string;
  receiveStatus: string;
  title: string;
  notation: string;
  sendStatusDesc: string;
  receiveStatusDesc: string;
}

export default () => {
  const [danhSach, setDanhSach] = useState<IVanBanRecord[]>([]);
  const [loading, setLoading] = useState(false);
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
    } catch (error) {
      message.error(error);
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
    } catch (error) {
      message.error(error);
    } finally {
      setLoading(false);
    }
  };

  return {
    danhSach,
    loading,
    limit,
    total,
    page,
    record,
    visibleForm,
    setDanhSach,
    setLimit,
    setTotal,
    setPage,
    setRecord,
    setVisibleForm,
    get_received_edoc_list,
    get_sent_edoc_list,
  };
};
