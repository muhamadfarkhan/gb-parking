<?php

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It is a breeze. Simply tell Lumen the URIs it should respond to
| and give it the Closure to call when that URI is requested.
|
*/

$router->get('/', function () use ($router) {
    return $router->app->version();
});

$router->post('/login','AuthController@login');

$router->get('/profile','UserController@profile');

$router->post('/tr-store','TransactController@store');

$router->get('/tr-list','ReportController@transact');

$router->get('/prod-list','MasterController@tblProd');

$router->get('/plg-list','MasterController@tblPlg');

