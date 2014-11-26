Dim objXL
Set objXL = CreateObject("Excel.Application")
With objXL
    .Workbooks.Open ("C:\Users\l.naga rajesh\Documents\sample_test_macro.xlsm")
    .Application.Run "new_one"
    .Application.Quit
End With
Set objXL = Nothing 