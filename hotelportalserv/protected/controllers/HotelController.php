<?php

class HotelController extends Controller
{
	public function actionIndex()
	{
		$this->render('index');
	}

	// Uncomment the following methods and override them if needed
	
	public function filters()
	{
		/*
			// return the filter configuration for this controller, e.g.:
		return array(
			'inlineFilterName',
			array(
				'class'=>'path.to.FilterClass',
				'propertyName'=>'propertyValue',
			),
		);
		*/
		return array('accessControl');
	}

	/*
	public function actions()
	{
		// return external action classes, e.g.:
		return array(
			'action1'=>'path.to.ActionClass',
			'action2'=>array(
				'class'=>'path.to.AnotherActionClass',
				'propertyName'=>'propertyValue',
			),
		);
	}
	*/
	
	public function accessRules()
	{
		return array(
				array('allow',  // allow all users to perform 'index' and 'view' actions
						'actions'=>array('index','view','CreateHotel','GetHotelById','GetHotelByName','GetHotelByCity',),
						'users'=>array('*'),
				),
				array('allow', // allow authenticated user to perform 'create' and 'update' actions
						'actions'=>array('create','update'),
						'users'=>array('@'),
				),
				array('allow', // allow admin user to perform 'admin' and 'delete' actions
						'actions'=>array('admin','delete'),
						'users'=>array('admin'),
				),
				array('deny',  // deny all users
						'users'=>array('*'),
				),
		);
	}
	
	public function actionCreateHotel() {
		
		$hotel_name = isset($_POST['name']) ? $_POST['name'] : '';
		$address= isset($_POST['address']) ? $_POST['address'] : '';
		$city = isset($_POST['city']) ? $_POST['city'] : '';
		$longitude = isset($_POST['longitude']) ? $_POST['longitude'] : '';
		$latitude = isset($_POST['latitude']) ? $_POST['latitude'] : '';
	
		$hotel = new Hotel();
		
		$hotel->name= $hotel_name;
		$hotel->address= $address;
		$hotel->city = $city;
		$hotel->longitude = $longitude ;
		$hotel->latitude = $latitude ;
		 
		$Criteria = new CDbCriteria();
		$Criteria->condition = "name ='" . $hotel_name . "'";
		 
		$hotelModel= Hotel::model()->find($Criteria);
		$result = array();
		if($hotelModel == NULL){
			$hotel->save();
			$result['response'] = true;
			$result['message'] = "Successfull";
			 
		}
		else{
			//echo '{"message":"Invalid Username/Password"}';
			$result['response'] = true;
			$result['message'] = "Failure";
		}
		header('Content-type: application/json');
		echo CJSON::encode($result);
	
		foreach (Yii::app()->log->routes as $route) {
			if($route instanceof CWebLogRoute) {
				$route->enabled = false; // disable any weblogroutes
			}
		}
	}
	
	public function actionGetHotelById(){
	
		$hotel_id = isset($_POST['id']) ? $_POST['id'] : '';
	
		$Criteria = new CDbCriteria();
		$Criteria->condition = "id ='" . $hotel_id . "'";
	
		$hotelModel= Hotel::model()->find($Criteria);
		 
		$result = array();
		if($hotelModel == NULL){
			//echo '{"message":"Invalid Username/Password"}';
			$result['response'] = true;
			$result['message'] = "Failure";
		}
		else{
			 
			$hotel_name=$hotelModel->name;
			$address=$hotelModel->address;
			$city=$hotelModel->city;
			$longitude=$hotelModel->longitude;
			$latitude=$hotelModel->latitude;
	
			$result['response'] = true;
			$result['message'] = "Successfull";
			$result['name']= $hotel_name;
			$result['address']= $address;
			$result['city']= $city;
			$result['longitude']=$longitude;
			$result['latitude']=$latitude;
	
		}
	
		 
		header('Content-type: application/json');
		echo CJSON::encode($result);
	
		foreach (Yii::app()->log->routes as $route) {
			if($route instanceof CWebLogRoute) {
				$route->enabled = false; // disable any weblogroutes
			}
		}
	}
	 
	 
	public function actionGetHotelByName(){
	
		$hotel_name = isset($_POST['name']) ? $_POST['name'] : '';
	
		$Criteria = new CDbCriteria();
		$Criteria->condition = "name ='" . $hotel_name . "'";
	
		$hotelModel= Hotel::model()->find($Criteria);
			
		$result = array();
		if($hotelModel == NULL){
			
			$result['response'] = true;
			$result['message'] = "Failure";
		}
		else{
	
			$hotel_id=$hotelModel->id;
			$address=$hotelModel->address;
			$city=$hotelModel->city;
			$longitude=$hotelModel->longitude;
			$latitude=$hotelModel->latitude;
	
			$result['response'] = true;
			$result['message'] = "Successfull";
			$result['id']= $hotel_id;
			$result['address']= $address;
			$result['city']= $city;
			$result['longitude']=$longitude;
			$result['latitude']=$latitude;
	
		}
	
			
		header('Content-type: application/json');
		echo CJSON::encode($result);
	
		foreach (Yii::app()->log->routes as $route) {
			if($route instanceof CWebLogRoute) {
				$route->enabled = false; // disable any weblogroutes
			}
		}
	}
	
	public function actionGetHotelByCity(){
	
		$city = isset($_POST['city']) ? $_POST['city'] : '';
		$city=  addcslashes($city,'%_');
		$Criteria = new CDbCriteria(array('condition'=>"city LIKE :city",
				'params' =>array(':city'=>"%$city")
		));
		 
		 
		$hotelModel= Hotel::model()->findAll($Criteria);
		$result = array();
		$hotel_data = array();
		if($hotelModel){
			foreach($hotelModel as $record) {
				$new_hotel = new Hotel();
				$new_hotel->id = $record->id;
				$new_hotel->name = $record->name;
				$new_hotel->address = $record->address;
				$new_hotel->city = $record->city ;
				$new_hotel->longitude =$record->longitude;
				$new_hotel->latitude =$record->latitude;
				 
				array_push($hotel_data, $new_hotel);
			}
			$result['response'] = true;
			$result['message'] = "Successfull";
	
		}
		else{
			//echo '{"message":"Invalid Username/Password"}';
			$result['response'] = true;
			$result['message'] = "Failure";
		}
		$result['hotels']= $hotel_data;
		 
	
		 
		header('Content-type: application/json');
		echo CJSON::encode($result);
	
		foreach (Yii::app()->log->routes as $route) {
			if($route instanceof CWebLogRoute) {
				$route->enabled = false; // disable any weblogroutes
			}
		}
	}
	
	
}