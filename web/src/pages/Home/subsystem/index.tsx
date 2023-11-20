
import { Badge, Col, Row, Button, Card } from 'antd';
import React, { useEffect, useState } from 'react';
import { useModel, history } from 'umi';

const SubSystem = () => {
  const pathname = window.location.pathname;
  const memberId = pathname.split('/')[2];
  const subsystemId = pathname.split('/')[4];

  const Xmodel = useModel('home');
  
  
  const handleOnClick = (item: any) => {
    let serviceCode = item.service_code;
    history.push(`/members/${memberId}/subsystems/${subsystemId}/services/${serviceCode}`);
  }

  const renderMember = (item: any) => {
    let serviceCode = item.service_code;
    return serviceCode;
  }
  
  useEffect(() => {
    // Xmodel.setData(xx_data);
    Xmodel.get_services({
      'Subsystem-Code': subsystemId,
      'X-Road-Client': 'CS/GOV/SS1MC/SS1SUB1'
    });
    console.log('useEffect', Xmodel?.serviceData?.filter((item) => item?.id?.object_type == 'MEMBER'));
  }, []);
  return (
    <div>
      {
        Xmodel?.serviceData?.map((item, index) => <Button style={{'display':'block','margin':'6px'}} key={index} type="primary" onClick={() => handleOnClick(item)}>{renderMember(item)}</Button>)
      }
    </div>
  )
};

export default SubSystem;
