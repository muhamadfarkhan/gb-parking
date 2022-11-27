<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\TblKrt;
use App\Models\TmpKrt;
use Exception;
use Validator;

class ReportController extends Controller
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
    public function transact(Request $request)
    {
        try {

            if(empty($request->search)){
                $this->data = TblKrt::join('tbl_prod','tbl_prod.PRODTYP','=','tbl_krt.PRODTYP')
						->where('tbl_prod.PRODFOR',2)
                        ->select('tbl_krt.*')
                        ->orderBy('lupddttime','desc')->get();
            }else{
                $this->data = TblKrt::join('tbl_prod','tbl_prod.PRODTYP','=','tbl_krt.PRODTYP')
                        ->select('tbl_krt.*')
						->where('tbl_prod.PRODFOR',2)
                        ->where('passno','like','%'.$request->search.'%')
                        ->orderBy('lupddttime','desc')->get();
            }
            

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
    public function transactDetail(Request $request)
    {
        try {

            $this->data = TblKrt::join('tbl_prod','tbl_prod.PRODTYP','=','tbl_krt.PRODTYP')
                    ->join('tbl_plg','tbl_plg.CUSTNO','=','tbl_krt.CUSTNO')
                    ->select('tbl_krt.*','PRODDES','FULLNM')
                    ->where('PASSNO',$request->passno) 
					//->Where('tbl_prod.PRODFOR','=','2')
                    ->orderBy('lupddttime','desc')->get();

        } catch (Exception $e) {
            $this->code = 500;
            $this->data = $e;
        }
        return $this->responseApi($this->code, $this->data);

    }
    
}