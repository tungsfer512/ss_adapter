
import { Badge, Col, Row, Button, Card } from 'antd';
import React, { useEffect, useState } from 'react';
import { useModel, history } from 'umi';

const Home = () => {

  const Xmodel = useModel('home');
  
  
  const handleOnClick = (item: any) => {
    let itemName = item?.id?.xroad_instance + ':' + item?.id?.member_class + ":" + item?.id?.member_code;
    history.push(`/members/${itemName}`);
  }

  const renderMember = (item: any) => {
    let itemName = item?.id?.xroad_instance + ':' + item?.id?.member_class + ":" + item?.id?.member_code;
    return itemName;
  }
  
  useEffect(() => {
    // Xmodel.setData(xx_data);
    Xmodel.get_members();
    console.log('useEffect', Xmodel?.data?.filter((item) => item?.id?.object_type == 'MEMBER'));
  }, []);

  return (
    <div>
      {
        Xmodel?.data?.filter((item) => item?.id?.object_type == 'MEMBER').map((item, index) => <Button style={{'display':'block','margin':'6px'}} key={index} type="primary" onClick={() => handleOnClick(item)}>{renderMember(item)}</Button>)
      }
    </div>
  )
};

export default Home;
