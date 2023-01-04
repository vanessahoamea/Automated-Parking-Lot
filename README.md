# API - Automated Parking Lot

## User endpoint.

###   Get list of users
####  Request
`GET /api/users`
```
curl -H "Authorization: Bearer <ACCESS_TOKEN>" https://automated-parking-lot.herokuapp.com/api/users
```
####  Response
Status: 200 OK
Content-Type: application/json
[User] - array of users

###   Save user
####  Request
`POST /api/user/save`
```
curl  -H 'Content-Type: application/json' -d '{"username":"user1", "password":"Strong122@", "name":"user", "email":"user@eu.ro"}'  https://automated-parking-lot.herokuapp.com/api/user/save
```
####  Response
Status: 201 OK
Content-Type: application/json
user - info

###   Log in
####  Request
`POST /api/login`
```
curl -H 'Content-Type: application/x-www-form-urlencoded' \ --data-urlencode 'email=address@mail.com' \ --data-urlencode 'password=Password123'  https://automated-parking-lot.herokuapp.com/api/user/login
```
####  Response
Status: 200 OK
Content-Type: application/json
{
	"access_token" : <NEW_ACCESS_TOKEN>,
	"refresh_token" :<REFRESH_TOKEN>
}

###   Update user
####  Request
`PUT /api/user`
```
curl -H "Authorization: Bearer <ACCESS_TOKEN>" -H 'Content-Type: application/json' -d '{"name":"new_name", "email":"newemail@mail.com"}'  https://automated-parking-lot.herokuapp.com/api/user
```
####  Response
Status: 200 OK
Content-Type: application/json
{
  "name": "new_name",
  "email": "newemail@mail.com"
}

###   Get user profile
####  Request
`GET /api/profile`
```
curl -X GET -H "Authorization: Bearer <ACCESS_TOKEN>" https://automated-parking-lot.herokuapp.com/api/user/profile
```
####  Response
Status: 200 OK
Content-Type: application/json
User - user infos


###   Add role to user
####  Request
`POST /api/add/role`
```
 curl  -H 'Content-Type: application/json' -d '{ "email":"user@eu.ro", "role":"USER"}'  https://automated-parking-lot.herokuapp.com/api/add/role
```

####  Response
Status: 200 OK
Role added.

### Get refresh token
####  Request
`GET /api/token/refresh`
````
 curl -H "Authorization: Bearer <REFRESH_TOKEN>" https://automated-parking-lot.herokuapp.com/api/token/refresh
````

####  Response
Status: 200 OK
{
	"access_token" : <NEW_ACCESS_TOKEN>,
	"refresh_token" :<REFRESH_TOKEN>
}

###   Get list of user cars
####  Request
`GET /api/user/cars`
```

curl -H "Authorization: Bearer <ACCESS_TOKEN>" https://automated-parking-lot.herokuapp.com/api/user/cars
```
####  Response
Status: 200 OK
Content-Type: application/json
[Car] - array of user cars

###   Banning user
####  Request
`POST /api/admin/banstatus`
```
 curl  -H 'Content-Type: application/json' -d '{ "email":"user@eu.ro", "banUser":"boolean"}'  https://automated-parking-lot.herokuapp.com/api/admin/banstatus
```

####  Response
Status: 200 OK
Banned user.

###   Banning provider
####  Request
`POST /api/admin/banstatus`
```
 curl  -H 'Content-Type: application/json' -d '{ "email":"user@eu.ro", "banProvider":"boolean"}'  https://automated-parking-lot.herokuapp.com/api/admin/banstatus
```

####  Response
Status: 200 OK
Banned provider.

## Car endpoint.
###   Save car
####  Request
`POST /api/user/car`
```
curl -X POST -H 'Content-Type: application/json' -H "Authorization: Bearer <ACCESS_TOKEN>" -H 'Content-Type: application/json' -d '{"licensePlate" : "VS06PST"}' https://automated-parking-lot.herokuapp.com/api/user/car
```
####  Response
Status: 201 OK
Content-Type: application/json
Car - info

####  Request
`GET /api/user/car/{id}`
```
curl -X GET https://automated-parking-lot.herokuapp.com/api/user/car/{id}
```
####  Response
Status: 200 OK
Content-Type: application/json
Car - the car with the id: {id}

###   Update car
####  Request
`Put /api/user/car`
```
curl -X PUT -H 'Content-Type: application/json' -d '{ "carId" : 5, "newLicensePlate" : "VS07PST"}' https://automated-parking-lot.herokuapp.com/api/user/car
```
####  Response
Status: 200 OK
Content-Type: application/json
Car - updates the car with the id "carId" with the new license plate "newLicensePlate"

###   Delete car
####  Request
`Delete /api/user/car/{id}`
```
curl -X DELETE https://automated-parking-lot.herokuapp.com/api/user/car/{id}
```
####  Response
Status: 200 OK
Content-Type: string "Car with id 5 deleted successfully."

## Parking lot endpoint.
###   Add parking lot
####  Request
`POST /api/provider/parkinglot/car`
```
curl -X POST -H 'Content-Type: application/json' -H "Authorization: Bearer <ACCESS_TOKEN>" -H 'Content-Type: application/json' -d '{"name" : "parc1", "latitude": 12.34, "longitude": 21.32244, "price": 43.2, "spots": [{"type": 1, "available": true}], {"type": 0}, {"type": 5}}' https://automated-parking-lot.herokuapp.com/api/provider/parkinglot/car
```
####  Response
Status: 201 OK
Content-Type: application/json
Parking lot - info

####  Request
###   Update parking lot
####  Request
`PUT /api/parkingspots/{id}`
```
curl -X POST -H 'Content-Type: application/json' -H "Authorization: Bearer <ACCESS_TOKEN>" -H 'Content-Type: application/json' -d '{"available": false}' https://automated-parking-lot.herokuapp.com/api/api/parkingspots/{id}
```
####  Response
Status: 200 OK
Content-Type: application/json
Parking lot - info
