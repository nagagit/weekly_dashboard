Dim objXL
Set objXL = CreateObject("Excel.Application")
With objXL
    .Workbooks.Open ("weekly_dashboard_macrobook.xlsb")
    .Application.Run "saveAsXls", "Mini_Dashboard.xlsx", "Mini_Dashboard.xls"
    .Application.Quit
End With
Set objXL = Nothing