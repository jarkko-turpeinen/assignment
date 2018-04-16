import React from 'react'

class Equipments extends React.Component {
  constructor (props) {
    super(props)    
    this.state = {        
      equipments: [{equipmentNumber: "1000000001", status: "Running"}]
    };
    this.handleChange = this.handleChange.bind(this);
  }

  search = (limit) => {
    return this.state.equipments
  }

  save = (equipment) => {
    return this.state.equipments
  } 

}

export default Equipments