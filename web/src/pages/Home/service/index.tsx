
import { Badge, Col, Row, Button, Card } from 'antd';
import React, { useEffect, useState } from 'react';
import { useModel, history } from 'umi';
import { toISOString } from '../../../utils/utils';

const Services = () => {
  const pathname = window.location.pathname;
  const memberId = pathname.split('/')[2];
  const subsystemId = pathname.split('/')[4];
  const serviceCode = pathname.split('/')[6];

  const Xmodel = useModel('home');
  
  const handleOnClick = (item: any) => {
    Xmodel.get_endpoint({
      'Subsystem-Code': subsystemId,
      'Service-Code': serviceCode,
      'X-Road-Client': 'CS/GOV/SS1MC/SS1IS1',
      'Service-Endpoint': item.path,
    })
  }

  const renderMember = (item: any) => {
    let serviceEndpoint = item.method + ' ' + item.path;
    return serviceEndpoint;
  }
  
  useEffect(() => {
    // Xmodel.setData(xx_data);
    Xmodel.get_services({
      'Subsystem-Code': subsystemId,
      'X-Road-Client': 'CS/GOV/SS1MC/SS1IS1'
    });
    console.log('useEffect', Xmodel?.serviceData?.filter((item) => item?.id?.object_type == 'MEMBER'));
  }, []);
  return (
    <div>
      {
        Xmodel?.serviceData?.filter((item) => (item.service_code == serviceCode))[0]['endpoint_list']?.map((item, index) => <Button style={{'display':'block','margin':'6px'}} key={index} type="primary" onClick={() => handleOnClick(item)}>{renderMember(item)}</Button>)
      }
      <div loading={Xmodel.loading}>
        {JSON.stringify(Xmodel.endpointData)}
      </div>
    </div>
  )
};

export default Services;
