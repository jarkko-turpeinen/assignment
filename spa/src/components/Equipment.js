import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css'
import { Card, CardText, CardBody, CardTitle } from 'reactstrap';
import EquimentForm from './EquipmentForm'

const Equipment = props => {
    return (
        <div>
            <Card>
                <CardBody>
                    <CardTitle>Eqipments</CardTitle>
                    <CardText>Save and search equipments</CardText>
                    <EquimentForm/>
                </CardBody>
            </Card>
        </div>
    )
}

export default Equipment