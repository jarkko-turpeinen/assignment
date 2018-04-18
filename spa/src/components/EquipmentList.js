import React from 'react';
import { CardBody, Card, Table } from 'reactstrap';

const EquipmentList = props => {
  return (
    <div>
      <Card>
        <CardBody>
        <Table>
          <thead>
            <tr>
              <th>Equipment Number</th>
              <th>Contract Start Date</th>
              <th>Contract End Date</th>
              <th>Address</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
          {props.equipments.map(equipment =>
            <tr key={equipment._id} >
              <th>{equipment.equipmentNumber}</th>
              <td>{equipment.contractStartDate}</td>
              <td>{equipment.contractEndDate}</td>
              <td>{equipment.address}</td>
              <td>{equipment.status}</td>
            </tr>
          )}
          </tbody>
        </Table>  
        </CardBody>
      </Card>
    </div>
  )
}

export default EquipmentList