<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Laravel Webcam</title>

     
     <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

     
     <script src="https://cdnjs.cloudflare.com/ajax/libs/webcamjs/1.0.26/webcam.min.js"></script>

     
     <script src="https://cdn.tailwindcss.com"></script>
</head>
<body>
    
    <div>
        <div class="max-w-[1150px] m-auto px-3"> 

            
            <div id="upload_section">
                <div class="text-center mt-5">
                    <div class="flex justify-center">
                        @if(session('selfie'))
                            <img src="{{asset('uploads/selfies/'.session('selfie'))}}" class="w-[270px] h-[270px]" alt="upload_dl">
                        @else
                            <img src="{{asset('images/upload_selfie.svg')}}" class="w-[270px] h-[270px]" alt="upload_dl">
                        @endif
                    </div>
                    <h3 class="text-[28px] font-bold mt-5">Take Selfie</h3>
                </div>
    
                <div class="flex justify-center h-[30vh] md:h-[10vh] items-center text-center mb-3 md:mt-5">
                    <p class="text-14px">
                        Click on the button below and take your selfie.
                        <br><br>
                        <span class="text-red-600">Please ensure you take the selfie in good light and the image is clear.</span>
                    </p>
                </div>
    
                <div class="flex justify-center w-[100%] md:w-[20%] m-auto mt-5 md:mt-10">
                    <button class="startCamera flex justify-between w-full rounded p-3 font-bold bg-[#000000] text-white">
                        TAKE SELFIE
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5"
                            stroke="currentColor" class="w-6 h-6">
                            <path stroke-linecap="round" stroke-linejoin="round"
                                d="M17.25 8.25 21 12m0 0-3.75 3.75M21 12H3" />
                        </svg>
                    </button>
                </div>
            </div>
    
            
            <div id="camera_section" class="hidden">
                
                <div class="text-center mt-5">
                    <div class="flex justify-center" style="height: 350px">
                        <div id="my_camera" style="width:320px; height:240px;"></div>
                    </div>
                </div>
    
                <div class="flex justify-center w-[100%] md:w-[20%] m-auto mt-5 md:mt-10">
                    <button id="capture" class="flex justify-between w-full rounded p-3 mt-8 md:mt-5 bg-[#000000] text-white">
                        Capture
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5"
                            stroke="currentColor" class="w-6 h-6">
                            <path stroke-linecap="round" stroke-linejoin="round"
                                d="M17.25 8.25 21 12m0 0-3.75 3.75M21 12H3" />
                        </svg>
                     </button>
                </div>
            </div>
    
            
            <div id="preview_section" class="hidden">
                <div class="flex justify-center mt-3  rounded-lg p-2">
                    <img src="" alt="preview" id="preview_img" class="rounded-full shadow h-[300px] w-[300px]">
                </div>
    
                <div class="mt-4 flex justify-center">
                    <button class="startCamera flex justify-start gap-1 text-[14px] font-bold text-[#303030]">
                        Retry
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none"  viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" class="w-5 h-5 ml-1 mb-1">
                            <path strokeLinecap="round" strokeLinejoin="round" d="M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0 3.181 3.183a8.25 8.25 0 0 0 13.803-3.7M4.031 9.865a8.25 8.25 0 0 1 13.803-3.7l3.181 3.182m0-4.991v4.99" />
                        </svg>
                    </button>
                </div>
    
                <form action="{{route('upload-selfie')}}" method="post">
                    @csrf
                    <div class="flex justify-center w-[100%] md:w-[20%] m-auto mt-5 md:mt-1">
                        <input type="hidden" name="selfie" id="selfieImg">
                        <button class="flex justify-between font-bold w-full rounded p-3 bg-[#000000] text-white">
                            CONFIRM & UPLOAD
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5"
                            stroke="currentColor" class="w-6 h-6">
                            <path stroke-linecap="round" stroke-linejoin="round"
                                d="M17.25 8.25 21 12m0 0-3.75 3.75M21 12H3" />
                        </svg>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    
    <script>

        $('.startCamera').click(function(){

            $('#upload_section').addClass('hidden');
            $('#preview_section').addClass('hidden');  
            $('#camera_section').removeClass('hidden');

            Webcam.set({
                width: 640,
                height: 400,
                image_format: 'png',
                jpeg_quality: 90
            });

            Webcam.attach('#my_camera');

        })

        document.getElementById('capture').addEventListener('click', function() {

            Webcam.snap(function(data_uri) {
                image_data_url=data_uri
                const photo = document.getElementById('preview_img');
                photo.src = image_data_url;
                $('#selfieImg').val(image_data_url);
                $('#camera_section').addClass('hidden');    
                $('#preview_section').removeClass('hidden');  
            });

        });
    </script>
</body>
</html>