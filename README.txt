Author : Vinojan Thiyagarajah
mail : vinojan.11@cse.mrt.ac.lk

Hotel Portal - Android Application
------------

Source code : https://github.com/Vinojan/HotelPortal/tree/master/HotelPortal

Hotel portal is an aAndroid application for adding/searching hotels in Sri Lanka. Android app 
consists of two main acitivities one for entering the details of the hotels and the other for city based 
search including sorting. 

When application starts its check for the GPS settings availability and ask the user to enable it.
	Main screen has two buttons: ADD and SEARCH buttons

Adding Hotel : ADD button
	
	 This only allows user to add a new Hotel to the database when user location is available.
	 Sometimes if the GPS setting is ON, user location cannot be detected. So user have to getout of big buildings or have move some distance.
	 If location is available then clicking ADD button, let user to move into the adding new hotel activity.

	 User location is detected and updated in the screen from location providers(GPS / Network Provider).
	 User have to fill all textfields.

	   After fill the edit text field, user have to clic ADD button submit the data to database.

	   Application check for the Network connectivity. If there is no connectivity, display an alert to the user to enable internet setting.


Searching Hotel : SEARCH button
	
	User can enter city to get hotels in that city.
	If the city is in the database, then available hotel stored in the database are displayed in the listview.

	Available hotels are displayed in a sorted order.
		Sorting method :
		 *Get the user location (longitude and latitude)
		 *Get each hotel location from database (longitude and latitude)
		 *Calculate the distance between user and hotel location.
		 	longitude_difference= user_longitude - hotel_longitude
		 	latitdue_difference = user_latitude - hotel_latitude
		 	distance= squre root of {( longitude_difference * longitude_difference )+ (latitdue_difference * latitdue_difference)}

		 	
		 	Hotel list sorted according to this order if user location is available.
		 	If user location is not available then sorted in hotel name alphabetical order.

-----------------------------------------------------------------------------------------------

Hotel Portel Service : PHP backend.
---------------------

For the HotelPortal android application databace access, I created the PHP backend using YII PHP MVC framework. 
(Ref: http://www.yiiframework.com/)

Through this framework I matched the database tables as Models and COntroler and Views.
Framework take care of connecting the database.

I used the MySQL database and appache2 server in ubuntu locally.
Use phpmyadmin to manage database locally.

To run the backend you can copy the following sorce code in you web accessible folder and create the database in the web host.

* Source code : https://github.com/Vinojan/HotelPortal/tree/master/hotelportalserv


To run successfully Hotel Portal app after creating and putting the database and the php source code into web accesible folder / host.

*----		You have to change the the "Configuration.java" file inside the  "com.vinojan.hotelportal" package as :
*				 public final static String HOME_URL="localhost";
*				 						change the local host to your web host name.


---------- Thank you ------------------ 