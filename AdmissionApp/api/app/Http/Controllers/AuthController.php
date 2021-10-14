<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\MstUser;
use App\Models\UserToken;
use Exception;
use Validator;

class AuthController extends Controller
{

    private $data, $code;

    public function __construct()
    {
        $this->code = 200;
        $this->data = [];
    }

    /**
     * Login user.
     *
     * @param  Request  $request
     * @return Response
     */
    public function login(Request $request)
    {
        try {
            $validator = Validator::make($request->all(), [
                'username' => ['required']
            ]);

            if ($validator->fails()) {
                return $this->responseApi(400, $validator->errors()->all());
            }

            $user = MstUser::where('USRNM',$request->username)->first();

            if (!empty($user)) {

                $token = $this->generateRandomString();
                
                $data['user'] = $user;
                $data['token'] = $token;
                $this->data = $data;

                UserToken::insert([
                    'token' => $token,'username' => $request->username
                ]);

            } else {
                return $this->responseApi(400, ['errors' => ['Username Dan Password Anda Salah']]);
            }
        } catch (Exception $e) {
            $this->code = 500;
            $this->data = $e;
        }
        return $this->responseApi($this->code, $this->data);

    }

    function generateRandomString($length = 80)
    {
        $karakkter = '012345678dssd9abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        $panjang_karakter = strlen($karakkter);
        $str = '';
        for ($i = 0; $i < $length; $i++) {
            $str .= $karakkter[rand(0, $panjang_karakter - 1)];
        }
        return $str;
    }
    
}