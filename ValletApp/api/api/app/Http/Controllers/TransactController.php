<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\TblKrt;
use App\Models\TmpKrt;
use App\Models\TblTrans;
use App\Models\TblProd;
use App\Models\TblPlg;
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
                'name' => ['required'],
                'custno' => ['required'],
                'prodtyp' => ['required'],
                'notran' => ['required'],
                'usrnm' => ['required']
            ]);

            if ($validator->fails()) {
                return $this->responseApi(400, $validator->errors()->all());
            }

            // $this->checkDuplicate($request);
            
            $prod = TblProd::where('prodtyp',$request->prodtyp)->first();
            $trans = TblTrans::where('NOTRAN',$request->notran)->orWhere('FACTNO',$request->notran)->first(); // ini udah bisa nyari by factno / notran bro
			
			if(empty($prod)){
				return $this->responseApi(400, 'data produk '.$request->prodtyp.' tidak ditemukan');
			}
			
			if(empty($trans)){
				return $this->responseApi(400, 'data no trans '.$request->notran.' tidak ditemukan');
			}

            $dataTbl['passno'] = strtoupper($request->passno);
            $dataTbl['factno'] = strtoupper($request->factno);
            $dataTbl['regno']  = strtoupper($request->regno);
            $dataTbl['remark']  = $request->name.'-'.$request->note;
            $dataTbl['usrnme']  = $request->usrnm;
            $dataTbl['custno']  = $request->custno;
            $dataTbl['flag']  = "A";
            $dataTbl['prodtyp']  = $request->prodtyp;
            $dataTbl['lupddttime']  = $todayDT;
            $dataTbl['vehclass']  = $prod->VEHCLASS;
            $dataTbl['startdt']  = $request->startdt;
            $dataTbl['enddt']  = $request->enddt;
            $dataTbl['totentry']  = $prod->TOTENTRY;
            $dataTbl['maxentry']  = $prod->MAXENTRY;
            $dataTbl['fixfee']  = $prod->FIXFEE;
            $dataTbl['nohol']  = $prod->NOHOL;
            $dataTbl['p1']  = $prod->P1;
            $dataTbl['p2']  = $prod->P2;
            $dataTbl['p3']  = $prod->P3;
            $dataTbl['p4']  = $prod->P4;
            $dataTbl['p5']  = $prod->P5;
            $dataTbl['p6']  = $prod->P6;
            $dataTbl['p7']  = $prod->P7;
            $dataTbl['pfrom1']  = $prod->PFROM1;
            $dataTbl['pfrom2']  = $prod->PFROM2;
            $dataTbl['pfrom3']  = $prod->PFROM3;
            $dataTbl['pfrom4']  = $prod->PFROM4;
            $dataTbl['pfrom5']  = $prod->PFROM5;
            $dataTbl['pto1']  = $prod->PTO1;
            $dataTbl['pto2']  = $prod->PTO2;
            $dataTbl['pto3']  = $prod->PTO3;
            $dataTbl['pto4']  = $prod->PTO4;
            $dataTbl['pto5']  = $prod->PTO5;

            $dataTmp['passno'] = $request->passno;
            $dataTmp['factno'] = $request->factno;
            $dataTmp['regno']  = $request->regno;
            $dataTmp['custno']  = $request->custno;
            $dataTmp['prodtyp']  = $request->prodtyp;
            $dataTmp['lupddttime']  = $todayDT;
            $dataTbl['vehclass']  = $prod->VEHCLASS;
            $dataTmp['startdt']  = $request->startdt;
            $dataTmp['enddt']  = $request->enddt;
            $dataTmp['mntcd']  = 'N';
            $dataTmp['usrnme']  = $request->usrnm;
            $dataTmp['usrnm']  = $request->name.'-'.$request->note;
            
            
            $dataPlat['REGNO']  = $request->factno;
            TblTrans::where('NOTRAN',$trans->NOTRAN)->update($dataPlat); // terus di sini where nya juga dari data notran di atas
            
			
            if ($this->checkDuplicate($request)){

                $dataTmp['mntcd']  = 'M';
                $tblKrt = TblKrt::where('passno',$request->passno)->update($dataTbl);
				$tmpKrt = TmpKrt::insert($dataTmp);
                //$tmpKrt = TmpKrt::where('passno',$request->passno)->update($dataTmp);
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
    
    public function search(Request $request)
    {
        try {

            $this->data = TblTrans::where('NOTRAN',$request->no_trans)->orWhere('FACTNO',$request->no_trans)
							->whereNull('DATETIMEOUT')->firstOrFail();

        } catch (Exception $e) {
            $this->code = 500;
            $this->data = $e;
        }
        return $this->responseApi($this->code, $this->data);

    }
    
}