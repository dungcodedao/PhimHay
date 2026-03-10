Pod::Spec.new do |spec|
    spec.name                     = 'composeApp'
    spec.version                  = '1.0.0'
    spec.homepage                 = 'https://github.com/JetBrains/compose-multiplatform'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'Compose Multiplatform App Module'
    spec.vendored_frameworks      = 'build/cocoapods/framework/ComposeApp.framework'
    spec.libraries                = 'c++', 'sqlite3'
    spec.ios.deployment_target = '15.0'
                
    spec.prepare_command = <<-CMD
        ./gradlew :composeApp:assembleDebugAppleFrameworkForXcode
    CMD
end
