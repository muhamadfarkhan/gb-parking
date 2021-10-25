<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\TblKrt;
use App\Models\TmpKrt;
use Exception;
use Validator;

class TransactController extends Controller
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
    public function store(Request $request)
    {
        try {

            date_default_timezone_set('Asia/Jakarta');

            $todayDT = date("Y-m-d H:i:s");
         
            $validator = Validator::make($request->all(), [
                'passno' => ['required'],
                'factno' => ['required'],
                'regno' => ['required'],
                'remark' => ['required'],
                'custno' => ['required'],
                'prodtyp' => ['required']
            ]);

            if ($validator->fails()) {
                return $this->responseApi(400, $validator->errors()->all());
            }

            // $this->checkDuplicate($request);

            $dataTbl['passno'] = $request->passno;
            $dataTbl['factno'] = $request->factno;
            $dataTbl['regno']  = $request->regno;
            $dataTbl['remark']  = $request->remark;
            $dataTbl['custno']  = $request->custno;
            $dataTbl['prodtyp']  = $request->prodtyp;
            $dataTbl['lupddttime']  = $todayDT;
            $dataTbl['vehclass']  = 'B';
            $dataTbl['startdt']  = $request->startdt;
            $dataTbl['enddt']  = $request->enddt;

            $dataTmp['passno'] = $request->passno;
            $dataTmp['factno'] = $request->factno;
            $dataTmp['regno']  = $request->regno;
            $dataTmp['custno']  = $request->custno;
            $dataTmp['prodtyp']  = $request->prodtyp;
            $dataTmp['lupddttime']  = $todayDT;
            $dataTmp['vehclass']  = 'B';
            $dataTmp['startdt']  = $request->startdt;
            $dataTmp['enddt']  = $request->enddt;
            
            
            if ($this->checkDuplicate($request)){

                $tblKrt = TblKrt::where('passno',$request->passno)->update($dataTbl);
                $tmpKrt = TmpKrt::where('passno',$request->passno)->update($dataTmp);
            }else{
                
                $tblKrt = TblKrt::insert($dataTbl);
                $tmpKrt = TmpKrt::insert($dataTmp);
            }

        } catch (Exception $e) {
            $this->code = 500;
            $this->data = $e;
        }
        return $this->responseApi($this->code, $this->data);

    }

    private function checkDuplicate($request){
        //->where('factno',$request->factno)->where('vehclass','B')
        $check = TblKrt::where('passno',$request->passno)->first();

        if(empty($check)){
            return false;
        }else{
            return true;
        }
    }
    
}