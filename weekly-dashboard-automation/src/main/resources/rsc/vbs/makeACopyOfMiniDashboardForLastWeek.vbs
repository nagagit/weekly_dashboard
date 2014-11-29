Dim objXL
Set objXL = CreateObject("Excel.Application")
With objXL
    .Workbooks.Open ("weekly_dashboard_macrobook.xlsb")
    .Application.Run "makeacopy", "Mini_Dashboard_lastwk", "Daily_Release_Dashboard_lastwk.xlsm"
    .Application.Quit
End With
Set objXL = Nothing