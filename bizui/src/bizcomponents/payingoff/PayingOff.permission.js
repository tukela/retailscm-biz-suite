

import React, { Component } from 'react'
import FontAwesome from 'react-fontawesome';
import { connect } from 'dva'
import moment from 'moment'
import BooleanOption from 'components/BooleanOption';
import { Row, Col, Icon, Card, Tabs, Table, Radio, DatePicker, Tooltip, Menu, Dropdown,Badge, Switch,Select,Form,AutoComplete,Modal } from 'antd'
import { Link, Route, Redirect} from 'dva/router'
import numeral from 'numeral'

import DashboardTool from '../../common/Dashboard.tool'
import PageHeaderLayout from '../../layouts/PageHeaderLayout'
import styles from './PayingOff.profile.less'
import DescriptionList from '../../components/DescriptionList';

import GlobalComponents from '../../custcomponents';
import PermissionSetting from '../../permission/PermissionSetting'
import appLocaleName from '../../common/Locale.tool'
const { Description } = DescriptionList;
const {defaultRenderExtraHeader}= DashboardTool


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////


const internalSummaryOf = (payingOff,targetComponent) =>{
    const userContext = null
	return (
	<DescriptionList className={styles.headerList} size="small" col="4">
<Description term="Id">{payingOff.id}</Description> 
<Description term="Who">{payingOff.who}</Description> 
<Description term="Paid Time">{ moment(payingOff.paidTime).format('YYYY-MM-DD')}</Description> 
<Description term="Amount">{payingOff.amount}</Description> 
	
      </DescriptionList>
	)
}


const renderPermissionSetting = payingOff => {
  const {PayingOffBase} = GlobalComponents
  return <PermissionSetting targetObject={payingOff}  targetObjectMeta={PayingOffBase}/>
}

const internalRenderExtraHeader = defaultRenderExtraHeader

class PayingOffPermission extends Component {


  componentDidMount() {

  }
  

  render() {
    // eslint-disable-next-line max-len
    const  payingOff = this.props.payingOff;
    const { id,displayName, employeeSalarySheetCount } = payingOff
    const cardsData = {cardsName:"Paying Off",cardsFor: "payingOff",cardsSource: payingOff,
  		subItems: [
    
      	],
  	};
    const renderExtraHeader = this.props.renderExtraHeader || internalRenderExtraHeader
    const summaryOf = this.props.summaryOf || internalSummaryOf
   
    return (

      <PageHeaderLayout
        title={`${cardsData.cardsName}: ${displayName}`}
        content={summaryOf(cardsData.cardsSource,this)}
        wrapperClassName={styles.advancedForm}
      >
      {renderExtraHeader(cardsData.cardsSource)}
      {renderPermissionSetting(cardsData.cardsSource)}
      
      </PageHeaderLayout>
    )
  }
}

export default connect(state => ({
  payingOff: state._payingOff,
}))(Form.create()(PayingOffPermission))

