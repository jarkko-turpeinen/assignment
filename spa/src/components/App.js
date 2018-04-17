import React, { Component } from 'react';
import './../assets/App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Card, CardText, CardBody, CardTitle } from 'reactstrap';
import EquipmentForm from '../components/EquipmentForm'

class App extends Component {
  render() {
    return (
      <div className="App">
        <Card>
            <CardBody>
                <CardTitle>Eqipments</CardTitle>
                <CardText>Search & Save equipments</CardText>
                <EquipmentForm/>
            </CardBody>
        </Card>
      </div>
    );
  }
}

export default App;
