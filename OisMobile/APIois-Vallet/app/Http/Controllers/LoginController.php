<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models as Md;


class LoginController extends Controller
{
    protected $transaction;

    public function __construct(
        Md\Transaction $transaction,
        Md\Shift $shift,
        Md\Parameter $parameter
    ){
        $this->transaction  = $transaction;
        $this->shift        = $shift;
        $this->parameter    = $parameter;
        $this->timestamp    = date("Y-m-d H:i:s");
    }
    /**
     * Test Awal
     */
    public function testAwal(Request $request)
    {
        $username = $request->email;
        if($username){

            $check = \App\Models\User::where('USRNM',$username)->first();

            if($check){

                $check_testAwal = $this->shift->where('INITNM',$username)
                                                ->where('STATUS','A')
                                                ->whereNotNull('SDTTMIN')
                                                ->first();
                if($check_testAwal){
                    $data['result'] = 'false';
                        $data['msg'] = 'Anda sudah test awal';
                        echo json_encode($data);
                }else{
                    $data['INITNM'] = $username;
                    $data['STATUS'] = 'A';
                    $data['CODE']   = 'USR';
                    $data['SDTTMIN'] = $this->timestamp;
                    $data['GATE'] = '2';
                    $data['POS'] = $check->GATEID;

                    $insert = $this->shift->create($data);

                    if($insert){
                        $data['result'] = 'true';
                        $data['msg'] = 'Berhasil Test Awal';
                        echo json_encode($data);
                    }else{
                        $data['result'] = 'false';
                        $data['msg'] = 'Gagal';
                        echo json_encode($data);
                    }
                }
                

            }else{
                $data['result'] = 'false';
                        $data['msg'] = 'Gagal';
                        echo json_encode($data);
            }

            


        }else{
              $data['result'] = 'false';
                        $data['msg'] = 'Gagal data not found';
                        echo json_encode($data);
        }
    }

     /**
     * Test Awal
     */
    public function testAkhir(Request $request)
    {

        $username = $request->email;
        if($username){

            $check = \App\Models\User::where('USRNM',$username)->first();

            if($check){

                $check_testAkhir = $this->shift->where('INITNM',$username)
                                                ->where('STATUS','A')
                                                ->whereNotNull('SDTTMIN')
                                                ->first();
                if($check_testAkhir){
                    $data['STATUS'] = 'P';
                    $data['SDTTMOUT'] = $this->timestamp;
                    
                    $update = $this->shift->where('INITNM',$username)
                                ->where('STATUS','A')
                                ->update($data);

                    if($update){
                        $data['result'] = 'true';
                        $data['msg'] = 'Berhasil test Akhir';
                        echo json_encode($data);
                    }else{
                        $data['result'] = 'false';
                        $data['msg'] = 'Gagal';
                        echo json_encode($data);
                    }
                }else{
                    $data['result'] = 'false';
                        $data['msg'] = 'Anda sudah test akhir';
                        echo json_encode($data);
                }
                

            }else{
                $data['result'] = 'false';
                        $data['msg'] = 'Gagal... data not found';
                        echo json_encode($data);
            }

            


        }else{
            $data['result'] = 'false';
                        $data['msg'] = 'Gagal data not found';
                        echo json_encode($data);
        }
        
    }

    /**
     * Login
     */
    public function login(Request $request)
    {
        // dd($request);
        $username = $request->email;
        $password = $request->password;
        $imei = $request->imei;
        if(isset($imei,$username)){
            $check = \App\Models\Parameter::where('IMEI',$imei)->first();

            $id = ($this->parameter->where('IMEI',"!=","")->count() > 0) ? ($this->parameter->where('IMEI',"!=","")->count() + 1) : 1 ;

            if($check){
                    $user = \App\Models\User::where('USRNM',$username)
                                    ->where('PASSWORD',$password)
                                    ->where('DEPTCD','0110')
                                    ->where('LVLCD','5')
                                    ->where('STATUS','A')
                                    ->first();
                    $gate = \App\Models\User::where('USRNM',$username)
                                    ->where('PASSWORD',$password)
                                    ->where('DEPTCD','0110')
                                    ->where('LVLCD','5')
                                    ->where('STATUS','A')
                                    ->where('GATEID',$check->CODE)
                                    ->first();
                    

                if(!$user){
                    $data['result'] = 'false';
        			$data['msg'] = 'Username atau password anda salah';
        			echo json_encode($data);
        			return;
                }

                if(!$gate){
                    $data['result'] = 'false';
        			$data['msg'] = 'Anda tidak ditugaskan di POS ini';
        			echo json_encode($data);
        			return;
                }

                if($gate){
                    $testAkhir = \App\Models\Shift::where('POS',$check->CODE)
                                                ->where('STATUS',"A")
                                                ->first();
                    if($testAkhir and $username != $testAkhir->INITNM){
                        
                        
                        $company = \App\Models\Company::first();
                        $user = \App\Models\User::where('USRNM',$testAkhir->INITNM)->first();
                        $data['result'] = "true";
                        
            			$data['company'] = $company->COMPNM;
            			$data['dataLogin'] = $user;
                        	$data['msg'] = "User sebelumnya ($user->INITNM | $user->FULLNM) belum melakukan test akhir!";
            			echo json_encode($data);
            			return;
                    }else{
                        
                        $company = \App\Models\Company::first();
                        
                        $data['result'] = 'true';
            			$data['msg'] = 'Success';
            			$data['company'] = $company->COMPNM;
            			$data['dataLogin'] = $user;
            			echo json_encode($data);
            			return;
                    }
                }

            }else{
                $data['FLDNM'] = 'GATEID';
                // $data['CODE']  = 'A'.$id;
                $data['CODE']  = 'A'.sprintf("%02s", $id);
                $data['IMEI']  = $imei;
                $data['LUPDDTTIME']  = $this->timestamp;
                $insert = $this->parameter->create($data);
                if($insert){
                    $data['result'] = 'true';
            			$data['msg'] = 'Success';
            			echo json_encode($data);
            			return;
                }else{
                    $data['result'] = 'false';
            			$data['msg'] = 'failure';
            			echo json_encode($data);
            			return;
                }
            }

        }else{
            	$data['msg'] = 'failure';
            			echo json_encode($data);
            			return;
        }
        

        
    }

  
}