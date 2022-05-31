import React, { useState } from 'react';
import { FaCamera } from 'react-icons/fa';
import styles from './AddParkingLot.module.css';

import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import Toolbar from '@mui/material/Toolbar';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import CloseIcon from '@mui/icons-material/Close';

export default function AddParkingLot({ setAddLot }) {
  const [name, setName] = useState('');
  const [location, setLocation] = useState('');
  const [price, setPrice] = useState('');
  const [spots, setSpots] = useState('');

  const addParkingLot = async () => {
    const [latitude, longitude] = location.split(' ');
    const res = await fetch('https://automated-parking-lot.herokuapp.com/api/provider/parkinglot/save', {
      method: 'POST',
      body: JSON.stringify({
        name,
        latitude: parseFloat(latitude),
        longitude: parseFloat(longitude),
        price: parseFloat(price),
        spots: new Array(spots).fill({
          type: 0,
          available: true
        })
      }),
      headers: {
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + localStorage.getItem('token')
      }
    });
    if (res.ok) {
      alert('Done!');
    }
    else {
      console.log(sessionStorage.getItem('token'));
      alert(await res.text());
    }
  };

  return (
    <div className={styles.container}>
      <Box sx={{ display: 'flex' }}>
        <Drawer
          sx={{
            width: 350,
            flexShrink: 0,
            '& .MuiDrawer-paper': {
              width: 350,
              background: '#004643',
              fontFamily: ' "Open Sans", sans-serif',
              color: '#537f7d',
              boxSizing: 'border-box'
            }
          }}
          variant="permanent"
          anchor="right"
        >
          <Toolbar />
          <main className={styles.main}>
            <h1 className={styles.name}>Add Parking Lot</h1>

            <button className={styles.CloseIcon} onClick={() => setAddLot(false)}>
              <CloseIcon className={styles.icon} />
            </button>

            <div className={styles.upload}>
              <FaCamera />
              <div className={styles.inner}>
                <input type="file" accept="image/png, image/jpeg" />
              </div>
            </div>

            <TextField
              label="Name"
              type="text"
              InputLabelProps={{
                shrink: true
              }}
              variant="filled"
              size="small"
              className={styles.TextField}
              onInput={(e) => setName(e.target.value)}
              value={name}
            />

            <TextField
              label="Location"
              type="text"
              InputLabelProps={{
                shrink: true
              }}
              variant="filled"
              size="small"
              className={styles.TextField}
              onInput={(e) => setLocation(e.target.value)}
              value={location}
            />

            <TextField
              label="Pricing"
              type="text"
              InputLabelProps={{
                shrink: true
              }}
              variant="filled"
              size="small"
              className={styles.TextField}
              onInput={(e) => setPrice(e.target.value)}
              value={price}
            />

            <TextField
              label="Number of Spots"
              type="text"
              InputLabelProps={{
                shrink: true
              }}
              variant="filled"
              size="small"
              className={styles.TextField}
              onInput={(e) => setSpots(e.target.value)}
              value={spots}
            />

            <Button
              variant="contained"
              className={styles.btn}
              onClick={addParkingLot}
            >
              Add Parking
            </Button>
          </main>
        </Drawer>
      </Box>
    </div>
  );
}
