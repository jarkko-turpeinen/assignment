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
              <tr>
                <th scope="row">1000000001</th>
                <td>2018-04-16</td>
                <td>2023-04-15</td>
                <td>127.0.0.1</td>
                <td>Running</td>
              </tr>
          </tbody>
        </Table>  
        </CardBody>
      </Card>
    </div>
  )
}

export default EquipmentList