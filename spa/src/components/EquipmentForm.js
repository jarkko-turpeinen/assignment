import React from 'react';
import { Col, Button, Form, FormGroup, Label, Input } from 'reactstrap';
import EquipmentList from './EquipmentList'
import ContractDate from './ContractDate'
import Equipments from './../containers/Equipments'

export default class EquipmentForm extends React.Component {

  render() {
    return (
      <Form>
        <FormGroup row>
          <Label sm={4} for="equipmentNumber">Equipment Number</Label>
          <Col>
            <Input type="text" id="equipmentNumber" />
          </Col>
        </FormGroup>
        <FormGroup row>
          <Label sm={4} for="constractStartDate">Contract Start Date</Label>
          <Col>
            <ContractDate id="constractStartDate" />
          </Col>
        </FormGroup>
        <FormGroup row>
          <Label sm={4} for="constractEndDate">Contract End Date</Label>
          <Col>
            <ContractDate id="constractEndDate" />
          </Col>
        </FormGroup>
        <FormGroup row>
          <Label sm={4} for="address">IP Address</Label>
          <Col>
            <Input type="text" id="address" />
          </Col>
        </FormGroup>
        <FormGroup row>
        <Label sm={4} for="status">Status</Label>
          <Col>
            <Input disabled type="text" id="status" />
          </Col>
        </FormGroup>
        <FormGroup check row>
          <Col sm={{ offset: 4 }}>
            <Button>Search</Button>
            <Button>Save</Button>
          </Col>
        </FormGroup>
        <FormGroup>
            <EquipmentList list={Equipments}/>
        </FormGroup>
      </Form>      
    );
  }
}