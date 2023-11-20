import { getEndpoint, getMember, getService } from '@/services/home';
import { ip } from '@/utils/ip';
import { useState } from "react";

export default () => {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<any[]>([]);
  const [serviceData, setServiceData] = useState<any[]>([]);
  const [endpointData, setEndpointData] = useState<any[]>([]);

  const get_members = async () => {
    try {
      // setLoading(true);
      console.log("==================");
      const res = await getMember();
      console.log("==================", res.data?.data);
      console.log("==================", res);
      if (res.status === 200) {
        setData(res.data?.data);
      }
      // setData(xx_data);
      console.log("==================", data);
      // setLoading(false);
    } catch (error) {
      console.log("================== LOILOILOI");
      setLoading(false);
      setData([]);
    }
  };
  const get_services = async (payload: any) => {
    try {
      // setLoading(true);
      console.log("==================", payload);
      const res = await getService(payload);
      // console.log("==================", res.data?.data);
      // console.log("==================", res);
      if (res.status === 200) {
        setServiceData(res.data?.data);
      }
      // setData(xx_data);
      console.log("==================", data);
      // setLoading(false);
    } catch (error) {
      console.log("================== LOILOILOI");
      setLoading(false);
      setServiceData([]);
    }
  };
  const get_endpoint = async (payload: any) => {
    try {
      // setLoading(true);
      console.log("==================", payload);
      const res = await getEndpoint(payload);
      // console.log("==================", res.data?.data);
      // console.log("==================", res);
      if (res.status === 200) {
        setEndpointData(res.data?.data);
      }
      // setData(xx_data);
      console.log("==================", data);
      // setLoading(false);
    } catch (error) {
      console.log("================== LOILOILOI");
      setLoading(false);
      setEndpointData([]);
    }
  };
  return {
    loading,
    setLoading,
    data,
    setData,
    get_members,
    serviceData, 
    setServiceData,
    get_services,
    endpointData,
    setEndpointData,
    get_endpoint,
  };
};
