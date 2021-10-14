<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\MstUser;
use Exception;
use Validator;

class UserController extends Controller
{

    private $data, $code;

    public function __construct()
    {
        $this->code = 200;
        $this->data = [];
        $this->middleware('auth');
    }

    /**
     * Profile user.
     *
     * @param  Request  $request
     * @return Response
     */
    public function profile(Request $request)
    {
        try {
            
            $user = MstUser::first();

            if (!empty($user)) {

                $data['user'] = $user;
                $this->data = $data;

            } else {
                return $this->responseApi(400, ['errors' => ['Unauthorized']]);
            }
            
        } catch (Exception $e) {
            $this->code = 500;
            $this->data = $e;
        }
        return $this->responseApi($this->code, $this->data);

    }
    
}