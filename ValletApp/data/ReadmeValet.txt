Nama Aplikasi 
Nama Table yg terkait dengan update data valet :
1. tbl_biaya
2. tbl_trans
3. mst_kom
4. tbl_usr

Proses :
Login
select * from tbl_usr where usrnm = 'xxx' and password = 'xxx' and deptcd = '0110' and lvlcd = '5'

search transaksi :
seperti di addmission querynya

proses transaksi :
input nopol : ...
pilih kelas kendaraan : select * from tbl_biaya where walkdes like '%valet' ( untuk dapat isi filed vehclass  contoh B,C,S dll) 

simpan : update tbl_trans set regno = 'nopol', vehclass = field tbl_biaya.vehclass, vltdatetime = 'tgljam pada saat proses', usrnmv = tbl_usr.usrnm

Cetak pertama :


      BUKTI VALET

 select compnm from mst_kom
 select coaddr from mst_kom

Id Trans : select notran from tbl_trans
Nopol    : .... ( Font ukuran lebih besar dan tebal )
Waktu    : ....
Petugas : select fullnm from mst_usr where yg login

Gambar mobil tampak atas
   |       |
  |         |
  |         |
  |         |
   |       |

Keterangan :







Petugas      Pemilik



______________________

Klik cetak Tahap 2

   BUKTI VALET

 select compnm from mst_kom
 select coaddr from mst_kom

Id Trans : select notran from tbl_trans
Nopol    : ....  ( Font ukuran lebih besar dan tebal )
Waktu    : ....
Petugas : select fullnm from mst_usr where yg login