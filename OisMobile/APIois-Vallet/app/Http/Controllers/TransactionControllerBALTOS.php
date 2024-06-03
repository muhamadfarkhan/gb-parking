<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models as Md;
use DateTime;
use View;


class TransactionController extends Controller
{
    protected $transaction;

    public function __construct(
        Md\Transaction $transaction,
        Md\Shift $shift
    ){
        $this->transaction    = $transaction;
        $this->shift        = $shift;
        $this->timestamp    = date("Y-m-d H:i:s");
    }
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        //
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        //
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function showTrans($id)
    {
        if(isset($id)){
            
            $checking = $this->transaction->where('NOTRAN',$id)
                        ->join('tbl_biaya','tbl_biaya.VEHCLASS','tbl_trans.VEHCLASS')
                        ->select('tbl_trans.*','tbl_biaya.WALKDES')
                        ->first();

            if(isset($checking)){

                $company = \App\Models\Company::first();

                $user = \App\Models\User::where('USRNM',$checking->USRNMA)->first();

                $data['company']   = $company->COMPNM;

                $datetime1 = strtotime($checking->DATETIMEIN);
                $datetime2 = strtotime($checking->DATETIMEOUT);
                $interval  = abs($datetime2 - $datetime1);
                $time   = round($interval / 60 / 60);

                $datetime1 = new DateTime($checking->DATETIMEIN);
                $datetime2 = new DateTime($checking->DATETIMEOUT);
                $interval = $datetime1->diff($datetime2);
                // if($time < 1440 ){
                //     $range = $interval->format('%h')." Jam ".$interval->format('%i')." Menit";
                // }else{
                    $range = $interval->format('%d')*24+$interval->format('%h')." Jam ".$interval->format('%i')." Menit";
                // }

                   
                $data['username']   = $user->FULLNM;
                $data['title']      = $id;
                $data['checkin']    = $checking->DATETIMEIN." ".$checking->USRNMA;
                $data['checkout']   = $checking->DATETIMEOUT." ".$user->GATEID;
                $data['regno']      = $checking->REGNO;
                $data['vehicle']    = $checking->WALKDES." | ".$checking->NONOTA;
                $data['fee']        = number_format($checking->CPSFEE,0,',',',');
                $data['time']        = $range;
                // return view("modules.transaction.detail",$data);

                return View::make('test')->with('data', json_encode($data));

            }else{
                // return "ID <i> $id </i> Not Found";
                return "failed";
            }

        }else{
            return "failed";
            // return "Please Provide an ID";
        }
    }

    /**
     * Checking ID Transaction.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function checkID(Request $request)
    {
        // dd($request);
        $id = $request->IDTransaction;
        // $regno = $request->REGNO;
        $username = $request->USERNAME;
     

        if(isset($id)){
            
            // ->where('NOTRAN',$id)
            //                             ->orWhere('REGNO',$id)
            $checking = $this->transaction->where(function($q) use ($id){
          $q->where('NOTRAN', $id)
            ->orWhere('REGNO', $id);
      })
                                        ->whereNull('DATETIMEOUT')
                                        ->first();


            if(isset($checking)){
                
                 $check_testAwal = $this->shift->where('INITNM',$request->USERNAME)                                                ->where('STATUS','A')
                     ->whereNotNull('SDTTMIN')
                     ->first();
                
                
            if($checking->DATETIMEOUT != null or $checking->DATETIMEOUT != ""){
                $data['result'] = 'false';
                $data['msg'] = 'Gagal. Data sudah di check out';
                echo json_encode($data);
            }elseif(!$check_testAwal){
                    $data['result'] = 'false';
                        $data['msg'] = 'Anda belum test awal';
                        echo json_encode($data);
                }else{
                
                

                $biaya = \App\Models\Biaya::where('VEHCLASS',$checking->VEHCLASS)->first();

                $company = \App\Models\Company::first();

                $user = \App\Models\User::where('USRNM',$check_testAwal->INITNM)->first();



                $datetime1 = strtotime($checking->DATETIMEIN);
                $datetime2 = strtotime($checking->DATETIMEOUT);
                $interval  = abs($datetime2 - $datetime1);
                $timeRange   = round($interval / 60 / 60);

                $datetime1 = new DateTime($checking->DATETIMEIN);
                $datetime2 = new DateTime($checking->DATETIMEOUT);
                $interval = $datetime1->diff($datetime2);
                // if($time < 1440 ){
                //     $range = $interval->format('%h')." Jam ".$interval->format('%i')." Menit";
                // }else{
                    $rangeTime = $interval->format('%d')*24+$interval->format('%h')." Jam ".$interval->format('%i')." Menit";
                // }


                $timeStart = $checking->DATETIMEIN;
                $timeEnd = $this->timestamp;

                // echo $timeStart."<br>";
                // echo $timeEnd."<br>";

                $datetime1 = strtotime($checking->DATETIMEIN);
                $datetime2 = strtotime($this->timestamp);
                $interval  = abs($datetime2 - $datetime1);
                $time   = round($interval / 60 / 60);

                $datetime1 = new DateTime($checking->DATETIMEIN);
                $datetime2 = new DateTime($checking->DATETIMEOUT);
                $interval = $datetime1->diff($datetime2);

                if($interval->format('%i') > 0){
                        $range = ($interval->format('%d')*24*60)+($interval->format('%h')*60)+60;
                    }else{
                        $range = ($interval->format('%d')*24*60)+($interval->format('%h')*60);
                    }

                // $data['CPSDATETIME']    = $this->timestamp; 
                $data['DATETIMEOUT']    = $this->timestamp; 
                $data['USRNMA']         = $username;
                $data['WSID']         = 'P';
                //$data['CPSLT']          = 'T';


                if($range <= $biaya->WHOUR1){
                    $result = $biaya->WRATE1;
                    // return $result;
                    //$data['CPSFEE']   = $result; 
                    $data['TRANFEE']   = $result; 

                    $update = $this->transaction->where('NOTRAN',$id)
                                        ->orWhere('REGNO',$id)
										->whereNull('DATETIMEOUT')
                                ->update($data);
                        if($update){
                            
                            //update counter
                            
                            $datacounter['LTRXVAL'] = $check_testAwal->LTRXVAL+1;
                            $datacounter['LINCVAL'] = $check_testAwal->LINCVAL+$result;

                            $this->shift->where('INITNM',$request->USERNAME)
                            ->where('STATUS','A')
                            ->whereNotNull('SDTTMIN')
                            ->update($datacounter);
                
                            
                            //end update counter
                            
                            
                            $data['result'] = 'true';
                            $data['msg'] = 'Success';
                            $data['FEE'] = $result;
                            $data['timerange'] = $rangeTime;
                            $data['jeniskend'] = $biaya->WALKDES;
                            $data['datas'] = $checking;
                            $data['company'] = $company->COMPNM;
                            $data['pic'] = $user->FULLNM;;
                            echo json_encode($data);
                        }else{
                            $data['result'] = 'false';
                            $data['msg'] = 'Gagal Proses Update';
                            echo json_encode($data);
                        }

                }elseif ($range > $biaya->WHOUR1 and $range <= $biaya->WHOUR2) {
                    $result = $biaya->WRATE1 + $biaya->WRATE2; 
                    //$data['CPSFEE']   = $result; 
                    $data['TRANFEE']   = $result; 

                    $update = $this->transaction->where('NOTRAN',$id)
                                        ->orWhere('REGNO',$id)
										->whereNull('DATETIMEOUT')
                                ->update($data);
                        if($update){

                        	//update counter
                            
                            $datacounter['LTRXVAL'] = $check_testAwal->LTRXVAL+1;
                            $datacounter['LINCVAL'] = $check_testAwal->LINCVAL+$result;

                            $this->shift->where('INITNM',$request->USERNAME)
                            ->where('STATUS','A')
                            ->whereNotNull('SDTTMIN')
                            ->update($datacounter);
                
                            
                            //end update counter

                            $data['result'] = 'true';
                            $data['msg'] = 'Success';
                            $data['FEE'] = $result;
                            $data['timerange'] = $rangeTime;
                            $data['datas'] = $checking;
                            $data['jeniskend'] = $biaya->WALKDES;
                            $data['company'] = $company->COMPNM;
                            $data['pic'] = $user->FULLNM;;
                            echo json_encode($data);
                        }else{
                             $data['result'] = 'false';
                            $data['msg'] = 'Gagal Proses Update';
                            echo json_encode($data);
                        }

                }elseif($range > $biaya->WHOUR2 and $biaya->WHOUR3 == "0"){
                    $ping = ($range + 60)-$biaya->WHOUR2;
                    // $fix = round($ping/$biaya->WHOUR2);
                    $result = $biaya->WRATE1+($ping/60*$biaya->WRATE2);
                    
                    //$data['CPSFEE']   = $result; 
                    $data['TRANFEE']   = $result; 

                    // echo $range;
                    // echo "<br>";
                    // echo $ping/60;
                    // echo "<br>";
                    // echo $result;
                    // echo "<br>";
                    // echo ($interval->format('%d')*24*60)+($interval->format('%h')*60);
                    $update = $this->transaction->where('NOTRAN',$id)
                                        ->orWhere('REGNO',$id)
										->whereNull('DATETIMEOUT')
                                ->update($data);
                        if($update){


     						//update counter
                            
                            $datacounter['LTRXVAL'] = $check_testAwal->LTRXVAL+1;
                            $datacounter['LINCVAL'] = $check_testAwal->LINCVAL+$result;

                            $this->shift->where('INITNM',$request->USERNAME)
                            ->where('STATUS','A')
                            ->whereNotNull('SDTTMIN')
                            ->update($datacounter);
                
                            
                            //end update counter

                            $data['result'] = 'true';
                            $data['msg'] = 'Success';
                            $data['FEE'] = $result;
                            $data['timerange'] = $rangeTime;
                            $data['jeniskend'] = $biaya->WALKDES;
                            $data['datas'] = $checking;
                            $data['company'] = $company->COMPNM;
                            $data['pic'] = $user->FULLNM;;
                            echo json_encode($data);
                        }else{
                             $data['result'] = 'false';
                            $data['msg'] = 'Gagal Proses Update';
                            echo json_encode($data);
                        }
                }else{
                     $data['result'] = 'false';
                    $data['msg'] = 'Gagal';
                    echo json_encode($data);
                }
                
            }

            }else{
                // return "ID <i> $id </i> Not Found";
                 $data['result'] = 'false';
                    $data['msg'] = 'Data tidak ditemukan';
                    echo json_encode($data);
            
            }

        }else{
             $data['result'] = 'false';
            $data['msg'] = 'Data tidak ada';
            echo json_encode($data);
            // return "Please Provide an ID";
        }
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function edit($id)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function insert(Request $request)
    {
        
        $check_testAwal = $this->shift->where('INITNM',$request->USERNAME)
                                                ->where('STATUS','A')
                                                ->whereNotNull('SDTTMIN')
                                                ->first();
                                                
        $check =   $this->transaction->where('REGNO',$request->REGNO)->whereNull('DATETIMEOUT')->first();



                if(!$check_testAwal){
                    $data['result'] = 'false';
                        $data['msg'] = 'Anda belum test awal';
                        echo json_encode($data);
                }else if($check and ($check->DATETIMEOUT == null or $check->DATETIMEOUT == "")){
                    // if($check->DATETIMEOUT == null or $check->DATETIMEOUT == ""){
                $data['result'] = 'false';
                $data['msg'] = 'Gagal. Nopol ini belum di check out';
                echo json_encode($data);
                // }
            }else{
                
                
        $input = $request->except('VEHCLASS');
        
        $veh = $request->VEHCLASS;
        $datetime = date('ymdh');
        $sec = date('s');
        
        $rand = rand(10,1000);
        
        $NOTRAN = $rand.$datetime.$sec;
        
        if($veh == 'Mobil Vallet'){
            $veh = '1';
        //}else if($veh == 'Mobil'){
        //    $veh = 'B';
        }else{
            $veh = 'X';
        }
        
        $input['VEHCLASS'] = $veh;
        $input['DATETIMEIN'] = $this->timestamp;
        
        $input['NOTRAN'] = $NOTRAN;
        $input['NONOTA'] = $rand;
        
        $inserting = $this->transaction->create($input);
        
        if($inserting){
            $data['result'] = 'true';
			$data['msg'] = 'Success';
			$data['NOTRAN'] = $NOTRAN;
			$data['JENIS'] = $request->VEHCLASS;
			$data['dataInsert'] = $input;
			echo json_encode($data);
        	
        }else{
            $data['result'] = 'false';
			$data['msg'] = 'Gagal';
			echo json_encode($data);
        }
                }
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        //
    }
}
