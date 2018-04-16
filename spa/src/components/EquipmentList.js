import React from 'react';
import { Table } from 'reactstrap';

export default class Example extends React.Component {
  render() {
    return (
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
            <th scope="row">1</th>
            <td>2018-04-16</td>
            <td>2023-04-16</td>
            <td>127.0.0.1</td>
            <td>Stopped</td>
          </tr>
        </tbody>
      </Table>
    );
  }
}