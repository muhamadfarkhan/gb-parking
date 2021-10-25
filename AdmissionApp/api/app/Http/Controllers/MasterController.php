<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\TblKrt;
use App\Models\TblProd;
use App\Models\TblPlg;
use Exception;
use Validator;

class MasterController extends Controller
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
    public function tblProd(Request $request)
    {
        try {

            $this->data = TblProd::where('passprdd','!=','00')
                                ->where('passprdd','!=','00')->get();

        } catch (Exception $e) {
            $this->code = 500;
            $this->data = $e;
        }
        return $this->responseApi($this->code, $this->data);

    }

    /**
     * Profile user.
     *
     * @param  Request  $request
     * @return Response
     */
    public function tblPlg(Request $request)
    {
        try {

            $this->data = TblPlg::get();

        } catch (Exception $e) {
            $this->code = 500;
            $this->data = $e;
        }
        return $this->responseApi($this->code, $this->data);

    }
    
}