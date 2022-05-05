import React from 'react';
import Background from 'assets/background.svg';
import styles from './Sidebar.module.css';

import Logo from 'assets/logo.svg';

import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import Toolbar from '@mui/material/Toolbar';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import DashboardIcon from '@mui/icons-material/Dashboard';
import AnalyticsIcon from '@mui/icons-material/Analytics';
import ConfirmationNumberIcon from '@mui/icons-material/ConfirmationNumber';
import ArticleIcon from '@mui/icons-material/Article';
import NotificationsIcon from '@mui/icons-material/Notifications';
import SettingsIcon from '@mui/icons-material/Settings';
import AccountBoxRoundedIcon from '@mui/icons-material/AccountBoxRounded';
import ExitToAppRoundedIcon from '@mui/icons-material/ExitToAppRounded';

const drawerWidth = 230;

export default function Sidebar(){
    return(

    <Box sx={{ display: 'flex'}} >

      <Drawer
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          '& .MuiDrawer-paper': {
            width: drawerWidth,
            background: "#004643",
            fontFamily: ' "Open Sans", sans-serif',
            color: "#537f7d",
            boxSizing: 'border-box',
          },
        }}
        variant="permanent"
        anchor="left"
      >
        <Toolbar />

        <img src={Logo} className={styles.logo} alt="logo" />
        <List>
          {['Dashboard', 'Analytics', 'Parkings', 'Schedule', 'Notification', 'Settings'].map((text, index) => (
            <ListItem button key={text}>
              { <ListItemIcon>
                    {index == 0 ? <DashboardIcon className={styles.icon} /> :
                     index == 1 ? <AnalyticsIcon className={styles.icon}/> :
                     index == 2 ? <ConfirmationNumberIcon className={styles.icon} /> :
                     index == 3 ? <ArticleIcon className={styles.icon}/> :
                     index == 4 ? <NotificationsIcon className={styles.icon}/> : <SettingsIcon className={styles.icon}/>
                     }

              </ListItemIcon> }
              <ListItemText primary={text} />
            </ListItem>
          ))}
        </List>


        {/* <br></br>
        <br></br>
        <br></br>
        <br></br>
        <br></br>

        <h4 className={styles.down}>Contact us</h4>
        <div>
            <AccountBoxRoundedIcon className={styles.profpic}/>
            <p>Easin Arafat</p>

        </div> */}


      </Drawer>

    </Box>

    );
}

