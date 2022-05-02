import os

PATH_TO_LOMBOK = os.path.expanduser(
  '~/.m2/repository/org/projectlombok/lombok/1.18.22/lombok-1.18.22.jar' )


def Settings( **kwargs ):
  if not os.path.exists( PATH_TO_LOMBOK ):
    raise RuntimeError( "Didn't find lombok jar!" )

  return {
    'jvm': {
      'args': [ '-javaagent:' + PATH_TO_LOMBOK,
                '-Xbootclasspath/p:' + PATH_TO_LOMBOK ]
    }
  }
