Nama Table yg terkait dengan penginputan data member :
Sebagai Table Master :
1. tbl_prod
2. tbl_plg
Sebagai Table Transaksi penginputan :
1. tbl_krt
2. tmp_krt

Add New :
No Plat Kendaraan : ..... ( save di table tbl_krt dan tmp_krt field : passno, factno, regno ) *
Nama 		  : ..... ( save di table tbl_krt dan tmp_krt field : remark ) *
Keterangan        : ..... ( save di table tbl_krt dan tmp_krt field : remark )
Kode Pelanggan    : ..... ( save di table tbl_krt dan tmp_krt field : custno ) relation ke table tbl_plg field custno. Tampil fullnm dalam aplikasi *
Kode Product	  : ..... ( save di table tbl_krt dan tmp_krt field : prodtyp ) relation ke table tbl_prod field prodtyp. Tampil proddes dalam aplikasi *
			  Note : Dalam table tbl_krt disimpan juga field dalam tbl_prod ( ada dalam contoh DB )
Masa Berlaku	  : ..... dd/mm/yyyy sd dd/mm/yyyy contoh : 01/09/2021 sd 03/09/2021
save juga di table tbl_krt dan tmp_krt field : lupddttime ( tanggal dan jam penginputan )

Edit :
Nama 		  : ..... ( update di table tbl_krt dan tmp_krt field : remark ) *
Keterangan        : ..... ( update di table tbl_krt dan tmp_krt field : remark )
Kode Pelanggan    : ..... ( update di table tbl_krt dan tmp_krt field : custno ) relation ke table tbl_plg field custno. Tampil fullnm dalam aplikasi *
Kode Product	  : ..... ( update di table tbl_krt dan tmp_krt field : prodtyp ) relation ke table tbl_prod field prodtyp. Tampil proddes dalam aplikasi *
			  Note : Dalam table tbl_krt diupdate juga field dalam tbl_prod ( ada dalam contoh DB )
Masa Berlaku	  : ..... dd/mm/yyyy sd dd/mm/yyyy contoh : 01/09/2021 sd 03/09/2021
update juga di table tbl_krt dan tmp_krt field : lupddttime ( tanggal dan jam penginputan )

Login aplikasi : table mst_usr field usrnm
				 