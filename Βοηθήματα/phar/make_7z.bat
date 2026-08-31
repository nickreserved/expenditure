@echo off
del bundle.jar
"C:\Program Files\7-Zip\7z.exe" a -tzip -mx=9 -mm=Deflate -mfb=258 bundle.jar php\.