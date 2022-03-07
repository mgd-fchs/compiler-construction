.source noSource
.class public Main
.super java/lang/Object

.method public <init>()V
  aload_0
  invokespecial java/lang/Object/<init>()V
  return
.end method

.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 6
  new Tower
  dup
  invokespecial Tower/<init>()V
  astore 1
  new Printer
  dup
  invokespecial Printer/<init>()V
  astore 2
  ldc 42
  istore 3
  aload 2
  iload 3
  invokevirtual Printer/printf(I)I
  istore 4
  aload 1
  invokevirtual Tower/resetNumber()I
  istore 4
  L_0:
  aload 1
  invokevirtual Tower/getNumber()I
  ldc 10
  if_icmplt L_1
  goto L_2
  L_1:
  aload 1
  iload 3
  invokevirtual Tower/multiply(I)I
  istore 3
  aload 2
  iload 3
  invokevirtual Printer/printf(I)I
  istore 4
  goto L_0
  L_2:
  aload 1
  invokevirtual Tower/resetNumber()I
  istore 4
  L_3:
  aload 1
  invokevirtual Tower/getNumber()I
  ldc 10
  if_icmplt L_4
  goto L_5
  L_4:
  aload 1
  iload 3
  invokevirtual Tower/divide(I)I
  istore 3
  aload 2
  iload 3
  invokevirtual Printer/printf(I)I
  istore 4
  goto L_3
  L_5:
  return
.end method
