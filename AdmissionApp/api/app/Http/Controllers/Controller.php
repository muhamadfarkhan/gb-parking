<?php

namespace App\Http\Controllers;

use Laravel\Lumen\Routing\Controller as BaseController;

class Controller extends BaseController
{
    protected function responseApi($code, $data)
    {
        $response['status'] = $code;

        if($code == 200){
            $response['message'] = "Success";
        }elseif($code == 404){
            $response['message'] = "Not Found";
        }elseif($code == 400){
            $response['message'] = "Bad Request";
        }elseif($code == 401){
            $response['message'] = "Unauthorized";
        }elseif($code == 405){
            $response['message'] = "Method Not Allowed";
        }else{
            $response['message'] = "Internal Server Error";
        }

        $response['data'] = $data;
        return response()->json($response, $code);
    }
}
