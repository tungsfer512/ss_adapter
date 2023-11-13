import { getEndpoint, getMember, getService } from '@/services/home';
import { ip } from '@/utils/ip';
import { useState } from "react";

export default () => {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<any[]>([]);
  const [serviceData, setServiceData] = useState<any[]>([]);
  const [endpointData, setEndpointData] = useState<any[]>([]);

const xx_data = [
    {
      "name": "MANAGE",
      "id": {
        "fields_for_string_format": [
          "GOV",
          "MANAGESSMC",
          null
        ],
        "member_code": "MANAGESSMC",
        "object_type": "MEMBER",
        "xroad_instance": "CS",
        "member_class": "GOV"
      }
    },
    {
      "name": "MANAGE",
      "id": {
        "subsystem_code": "MANAGEMENT",
        "fields_for_string_format": [
          "GOV",
          "MANAGESSMC",
          "MANAGEMENT"
        ],
        "member_code": "MANAGESSMC",
        "object_type": "SUBSYSTEM",
        "xroad_instance": "CS",
        "member_class": "GOV"
      }
    },
    {
      "name": "MANAGE",
      "id": {
        "subsystem_code": "MANAGEIS1",
        "fields_for_string_format": [
          "GOV",
          "MANAGESSMC",
          "MANAGEIS1"
        ],
        "member_code": "MANAGESSMC",
        "object_type": "SUBSYSTEM",
        "xroad_instance": "CS",
        "member_class": "GOV"
      }
    },
    {
      "name": "SS1",
      "id": {
        "fields_for_string_format": [
          "GOV",
          "SS1MC",
          null
        ],
        "member_code": "SS1MC",
        "object_type": "MEMBER",
        "xroad_instance": "CS",
        "member_class": "GOV"
      }
    },
    {
      "name": "SS1",
      "id": {
        "subsystem_code": "SS1IS1",
        "fields_for_string_format": [
          "GOV",
          "SS1MC",
          "SS1IS1"
        ],
        "member_code": "SS1MC",
        "object_type": "SUBSYSTEM",
        "xroad_instance": "CS",
        "member_class": "GOV"
      }
    }
  ]

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
