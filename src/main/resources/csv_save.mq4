extern bool EnterOpenBar = true;
int CurrentTime;

int init(){
CurrentTime= Time[0];
return(0);
}
int start(){

if (EnterOpenBar = true)
{
if(CurrentTime != Time[0]){
// first tick of new bar found
//Alert(Symbol(), " M", Period(), " Save");
int f;

string file="export_"+Symbol()+"__"+Period()+"__"+".csv";
f=FileOpen(file,FILE_CSV|FILE_WRITE,",");

while(f<1){
Sleep(5000);
f=FileOpen(file,FILE_CSV|FILE_WRITE,",");
}

   for(int i=0;i<Bars;i++) {
   FileWrite(f,Open[i],High[i],Low[i],Close[i],TimeToStr(Time[i],TIME_DATE|TIME_MINUTES),Symbol(),Period());
   }
   //Alert("Eksport "+Symbol()+" zakończony. Wyeksportowano "+Bars+" rekordów");
   FileFlush(f);
   FileClose(f);
CurrentTime= Time[0];
return(0);
}
}
}

