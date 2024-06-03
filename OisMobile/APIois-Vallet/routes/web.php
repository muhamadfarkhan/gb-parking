<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});

Route::post('/login_p', 'LoginController@login');

Route::post('/testAwal', 'LoginController@testAwal');
Route::post('/testAkhir', 'LoginController@testAkhir');

Route::post('/id', 'TransactionController@checkID');

Route::post('/insert_p', 'TransactionController@insert');

Route::get('/show/{id}', 'TransactionController@showTrans');