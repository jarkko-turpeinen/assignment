import React from 'react';
import { Col, Button, Form, FormGroup, Label, Input } from 'reactstrap';
import EquipmentList from './EquipmentList'
import ContractDate from './ContractDate'

export default class EquipmentForm extends React.Component {
  render() {
    return (
      <Form>
        <FormGroup row>
          <Label for="equipmentNumber" sm={2}>Equipment Number</Label>
          <Col sm={2}>
            <Input type="text" id="equipmentNumber" />
          </Col>
        </FormGroup>
        <FormGroup row>
          <Label for="constractStartDate" sm={2}>Contract Start Date</Label>
          <Col sm={2}>
            <ContractDate id="constractStartDate" />
          </Col>
        </FormGroup>
        <FormGroup row>
          <Label for="constractEndDate" sm={2}>Contract End Date</Label>
          <Col sm={2}>
            <ContractDate id="constractEndDate" />
          </Col>
        </FormGroup>
        <FormGroup row>
          <Label for="address" sm={2}>IP Address</Label>
          <Col sm={2}>
            <Input type="text" id="address" />
          </Col>
        </FormGroup>
        <FormGroup tag="fieldset" row>
          <legend className="col-form-label col-sm-2">Status</legend>
          <Col sm={2}>
            <FormGroup check>
              <Label check>
                <Input type="radio" name="status" />{' '}
                Running
              </Label>
            </FormGroup>
            <FormGroup check>
              <Label check>
                <Input type="radio" name="status" />{' '}
                Stopped
              </Label>
            </FormGroup>
          </Col>
        </FormGroup>
        <FormGroup check row>
          <Col sm={{ size: 2, offset: 2 }}>
            <Button>Save</Button>
          </Col>
        </FormGroup>
        <FormGroup>
            <EquipmentList/>
        </FormGroup>
      </Form>      
    );
  }
}