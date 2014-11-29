Dim objXL
Set objXL = CreateObject("Excel.Application")
With objXL
    .Workbooks.Open ("weekly_dashboard_macrobook.xlsb")
    .Application.Run "makeacopy", "Mini_Dashboard", "Daily_Release_Dashboard.xlsm"
    .Application.Quit
End With
Set objXL = Nothing