<?php

namespace App\Http\Controllers;
use PDF;
use Illuminate\Http\Request;

class PDFController extends Controller
{
    //
     public function generatePDF()
    {
        $data = [
            'title' => 'Laravel PDF Example',
            'date' => date('m/d/Y'),
        ];

        $pdf = PDF::loadView('myPDF', $data);

        return $pdf->download('document.pdf');
    }
}
