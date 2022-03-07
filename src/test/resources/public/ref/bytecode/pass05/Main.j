.source noSource
.class public Main
.super java/lang/Object

.method public static main([Ljava/lang/String;)V
.limit stack 2
.limit locals 3
  new Nested
  dup
  invokespecial Nested/<init>()V
  astore 1
  aload 1
  invokevirtual Nested/nestedIsland()I
  istore 2
  iload 2
  invokestatic java/lang/System/exit(I)V
  return
.end method
