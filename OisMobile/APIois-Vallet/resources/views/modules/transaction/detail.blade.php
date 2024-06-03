<html>
<head>
  <title>{{ $title }}</title>
  <meta name="csrf-token" content="{{ csrf_token() }}">
  <style type="text/css">
  
.tg  {border-collapse:collapse;border-spacing:0;border:none;}
.tg td{font-family:Arial, sans-serif;font-size:14px;padding:3px 11px;border-style:solid;border-width:0px;overflow:hidden;word-break:normal;}
.tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:3px 11px;border-style:solid;border-width:0px;overflow:hidden;word-break:normal;}
.tg .tg-baqh{text-align:center;vertical-align:top}
.tg .tg-yw4l{vertical-align:top}
.tg .tg-9hbo{font-weight:bold;vertical-align:top}
</style>
</head>
<body>
<table class="tg">
  <tr>
    <th class="tg-031e" colspan="3">{{ $company }}<br></th>
  </tr>
  <tr>
    <td class="tg-yw4l">No. Plat<br></td>
    <td class="tg-baqh">:</td>
    <td class="tg-yw4l">{{ $regno }}<br></td>
  </tr>
  <tr>
    <td class="tg-yw4l">Kend</td>
    <td class="tg-baqh">:<br></td>
    <td class="tg-yw4l">{{ $vehicle }}</td>
  </tr>
  <tr>
    <td class="tg-yw4l">Masuk</td>
    <td class="tg-baqh">:</td>
    <td class="tg-yw4l">{{ $checkin }}</td>
  </tr>
  <tr>
    <td class="tg-yw4l">Keluar</td>
    <td class="tg-baqh">:</td>
    <td class="tg-yw4l">{{ $checkout }}</td>
  </tr>
  <tr>
    <td class="tg-yw4l">Petugas</td>
    <td class="tg-baqh">:</td>
    <td class="tg-yw4l">{{ $username }}</td>
  </tr>
  <tr>
    <td class="tg-yw4l">Lama</td>
    <td class="tg-baqh">:<br></td>
    <td class="tg-yw4l">{{ $time }}</td>
  </tr>
  <tr>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-9hbo">TOTAL</td>
    <td class="tg-9hbo" colspan="2">Rp. {{ $fee }}<br></td>
  </tr>
  <tr>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-baqh" colspan="3">Terima Kasih Atas Kunjugan Anda<br></td>
  </tr>
</table>
</body>
</html> 

